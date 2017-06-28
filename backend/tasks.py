from celery import Celery
from celery.schedules import crontab
from urls import *
from games import *
from db_service import DBService
from scrap_data import scrap_matches

#celery -A tasks beat

app = Celery('tasks', backend='redis://localhost:6379', broker='redis://localhost:6379/0')
app.timezone = 'Europe/Moscow'
db_service = DBService.inst()

@app.on_after_configure.connect
def setup_periodic_tasks(sender, **kwargs):
    sender.add_periodic_task(
        crontab(minute=5),
        save_matches.s()
    )


@app.task
def save_matches():
    matches = list()
    matches.extend(scrap_matches(dota_matches_url, DOTA))
    matches.extend(scrap_matches(cs_matches_url, CSGO))
    db_service.update_matches(matches)
    print(matches)

