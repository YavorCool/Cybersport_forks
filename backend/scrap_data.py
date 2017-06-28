import requests
from lxml import html
from bookmakers import *
from urls import *
from games import *
from date import format_datetime
from db_service import DBService
# XPATH необходимых элементов
url_xpath = '//*[@id="block_matches_current"]/table/tr[{}]/td[2]/a/@href'
raws_xpath = '//*[@id="block_matches_current"]/table/tr'
bookmaker_logo_url_xpath = '//*[@id="pastkef"]/div[{}]/div[1]/a/@href'
bookmaker_raws_xpath = '//*[@id="pastkef"]/div'
coef_xpath = '//*[@id="pastkef"]/div[{}]/div[2]/table/tr/td[2]/b[{}]'
bookmaker_url_xpath = '//*[@id="pastkef"]/div[{}]/div[3]/a/@href'
team1_xpath = '//*[@id="page-content-wrapper"]/div/div/div[1]/div[3]/div/div[1]/div/h3/a'
team2_xpath = '//*[@id="page-content-wrapper"]/div/div/div[1]/div[3]/div/div[3]/div/h3/a'
date_time_xpath = '//*[@id="page-content-wrapper"]/div/div/div[1]/h3/label/time/@datetime'


#Возвращает список url с текущими матчами
def get_urls(matches_list_url, root_url):
    urls = []
    page_src = requests.get(matches_list_url)
    tree = html.fromstring(page_src.content)
    raws = tree.xpath(raws_xpath)
    if raws:
        counter = 0
        for raw in raws:
            match_url = tree.xpath(url_xpath.format(counter))
            if match_url:
                match_url = match_url[0]
                urls.append(root_url + match_url)
            counter += 1
    else:
        return None
    return urls


# Возвращает матч в виде:
# {'bets':
#      [{
#           'url': '/go?to=gg&k1&m=48673',      url, чтобы сделать ставку у букмекера
#           'coefs': ['1.91', '1.79'],          Коэффиценты на команду_1 и команду_2 соответственно
#           'title': 'GGBet'                    Название букмекера
#       },
#
#       {
#           'url': '/go?to=egb&k1&m=14182',      url, чтобы сделать ставку у букмекера
#           'coefs': ['2.36', '1.49'],           Коэффиценты на команду_1 и команду_2 соответственно
#           'title': 'EGB'                       Название букмекера
#       }
#      ],
#  'teams': ['ProDota', 'Mouz'],               Названия команд
#  'datetime': '2017-05-27T18:00-15:23'        Дата/Время проведения
#  'fork': [0.30, 0.5, 0.5, 2.88, 2.84]                                  Вилка
# }

def scrap_match(match_url, game):
    match = {'game': game, 'bets': [], 'fork': []}
    page_src = requests.get(match_url)
    tree = html.fromstring(page_src.content)
    raws = tree.xpath(bookmaker_raws_xpath)
    match['teams'] = [
        tree.xpath(team1_xpath)[0].text,
        tree.xpath(team2_xpath)[0].text,
    ]
    if raws:
        pattern = 'to={}'
        counter = 1
        bets = []
        for raw in raws:
            url = tree.xpath(bookmaker_logo_url_xpath.format(counter))[0]
            if pattern.format(short_title[EGB_TITLE]) in url:
                bets.append({"coefs": [
                    tree.xpath(coef_xpath.format(counter, 1))[0].text,
                    tree.xpath(coef_xpath.format(counter, 2))[0].text,
                ],
                    "url": url,
                    "title": EGB_TITLE
                })
            elif pattern.format(short_title[GGBET_TITLE]) in url:
                bets.append({"coefs": [
                    tree.xpath(coef_xpath.format(counter, 1))[0].text,
                    tree.xpath(coef_xpath.format(counter, 2))[0].text,
                ],
                    "url": url,
                    "title": GGBET_TITLE})
            else:
                continue

            counter += 1
        match['bets'] = bets
        match['fork'] = get_fork(match)
    match['datetime'] = format_datetime(tree.xpath(date_time_xpath)[0])
    return match


def get_fork(match):
    bets = match['bets'].copy()
    ggbets = None
    egbbet = None

    for bet in bets:
        if GGBET_TITLE == bet["title"]:
            ggbets = bet.copy()
        if EGB_TITLE == bet["title"]:
            egbbet = bet.copy()

    if ggbets and egbbet:
        ggbets[0] = float(ggbets["coefs"][0])
        ggbets[1] = float(ggbets["coefs"][1])
        egbbet[0] = float(egbbet["coefs"][0])
        egbbet[1] = float(egbbet["coefs"][1])

        sum_1 = 1/ggbets[0] + 1/egbbet[1]
        sum_2 = 1/ggbets[1] + 1/egbbet[0]

        if sum_1 < 1 or sum_2 < 1:
            if sum_1 < sum_2:
                sum = sum_1
                bet_2 = ggbets[0]/(ggbets[0] + egbbet[1])
                bet_1 = 1 - bet_2
                coefs = [ggbets[0], egbbet[1]]
            else:
                sum = sum_2
                bet_2 = egbbet[0]/(ggbets[1] + egbbet[0])
                bet_1 = 1 - bet_2
                coefs = [egbbet[0], ggbets[1]]

            return ["%.2f" % (1-sum), "%.2f" % bet_1, "%.2f" % bet_2, "%.2f" % coefs[0], "%.2f" % coefs[1]]
    return []


def scrap_matches(matches_url, game):
    matches = list()
    urls = get_urls(matches_url, root_url)

    for url in urls:
        match = scrap_match(url, game)
        if match:
            matches.append(match)

    return matches




# Test
if __name__ == "__main__":
    dota_2_matches = scrap_matches(dota_matches_url, DOTA)
    csgo_matches = scrap_matches(cs_matches_url, CSGO)
    dota_2_matches.extend(csgo_matches)
    db_service = DBService()
    db_service.update_matches(dota_2_matches)
    print(db_service.get_matches(DOTA))
    print(db_service.get_matches(CSGO))