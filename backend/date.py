months = {
    '01': 'января',
    '02': 'февраля',
    '03': 'марта',
    '04': 'апреля',
    '05': 'мая',
    '06': 'июня',
    '07': 'июля',
    '08': 'августа',
    '09': 'сентября',
    '10': 'октября',
    '11': 'ноября',
    '12': 'декабря',
 }


def format_datetime(datetime):
    time = datetime.split('T')[1].split('-')[0]
    raw_date = datetime.split('T')[0].split('-')

    return time + ' ' + raw_date[2] + ' ' + months[raw_date[1]] + ' ' + raw_date[0]


if __name__ == "__main__":
    a = "2017-06-03T17:00-08:44"
    print(format_datetime(a))


