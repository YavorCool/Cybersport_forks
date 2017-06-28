from pymongo import MongoClient
from games import *

DB_URI = 'mongodb://localhost:27017/'


class DBService:
    __instance = None

    @staticmethod
    def inst():
        if DBService.__instance is None:
            DBService.__instance = DBService()
        return DBService.__instance

    def __init__(self):
        self.client = MongoClient(DB_URI, connect=False)
        self.db = self.client.cybbet_db
        self.matches = self.db.matches

    def clear_db(self):
        self.matches.remove({}, {"justOne": False})

    def update_matches(self, matches):
        self.clear_db()
        self.matches.insert_many(matches)

    def get_matches(self, game):
        matches = list()
        for match in self.matches.find({'game': game}, {"_id": 0}):
            matches.append(match)
        return matches


# Testing
if __name__ == "__main__":
    db_service = DBService.inst()
    matches = [
        {'bets':
            [{
                'url': '/go?to=gg&k1&m=48673',
                'GGBet': ['1.91', '1.79']
            },

                {
                    'EGB': ['2.36', '1.49'],
                    'url': '/go?to=egb&k1&m=48673'
                }
            ],
            'teams': ['team1_1', 'team2_1'],
            'datetime': '2017-05-27T18:00-15:23',
            'has_fork': True,
            'game': 'dota_2'
        },
        {'bets':
            [{
                'url': '/go?to=gg&k1&m=48673',
                'GGBet': ['1', '2']
            },

                {
                    'EGB': ['1', '1.5'],
                    'url': '/go?to=egb&k1&m=48673'
                }
            ],
            'teams': ['team1_2', 'team2_2'],
            'datetime': '2017-05-27T18:00-15:23',
            'has_fork': True,
            'game': 'dota_2'
        },
        {'bets':
            [{
                'url': '/go?to=gg&k1&m=48673',
                'GGBet': ['3', '4']
            },

                {
                    'EGB': ['2', '5'],
                    'url': '/go?to=egb&k1&m=48673'
                }
            ],
            'teams': ['team3_1', 'team3_2'],
            'datetime': '2017-05-27T18:00-15:23',
            'has_fork': True,
            'game': 'csgo'
        },
    ]

    db_service.update_matches(matches)
    print(db_service.get_matches(CSGO))
    print(db_service.get_matches(DOTA))

