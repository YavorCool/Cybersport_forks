from db_service import DBService
from flask import Flask
from flask import jsonify
import json
import logging
import logging.handlers
from games import *

app = Flask(__name__)
db_service = DBService.inst()

f = logging.Formatter(fmt='%(levelname)s:%(name)s: %(message)s '
                          '(%(asctime)s; %(filename)s:%(lineno)d)',
                      datefmt="%Y-%m-%d %H:%M:%S")
handlers = [
    logging.handlers.RotatingFileHandler('rotated.log', encoding='utf8',
                                         maxBytes=100000, backupCount=1),
    logging.StreamHandler()
]
root_logger = logging.getLogger()
root_logger.setLevel(logging.DEBUG)
for h in handlers:
    h.setFormatter(f)
    h.setLevel(logging.DEBUG)
    root_logger.addHandler(h)


@app.route('/dota-2', methods=['GET'])
def get_dota2_matches():
    matches = db_service.get_matches(DOTA)
    return jsonify(matches)


@app.route('/csgo', methods=['GET'])
def get_csgo_matches():
    matches = db_service.get_matches(CSGO)
    return jsonify(matches)

if __name__ == "__main__":
    app.run(host='0.0.0.0', port='8000')