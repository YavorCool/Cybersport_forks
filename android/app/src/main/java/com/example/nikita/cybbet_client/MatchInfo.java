package com.example.nikita.cybbet_client;

public class MatchInfo {
    private String teams;
    private String datetime;
    private String[] fork;
    private String[] bets;
    private String[] urls;

    public MatchInfo(String teams, String datetime, String[] fork, String[] bets, String[] urls) {
        this.teams = teams;
        this.datetime = datetime;
        this.fork = fork;
        this.bets = bets;
        this.urls = urls;
    }

    public String getTeams() {
        return teams;
    }

    public void setTeams(String teams) {
        this.teams = teams;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String[] getFork() {
        return fork;
    }

    public void setFork(String[] fork) {
        this.fork = fork;
    }

    public String[] getBets() {
        return bets;
    }

    public void setBets(String[] bets) {
        this.bets = bets;
    }

    public String[] getUrls() {
        return urls;
    }

    public void setUrls(String[] urls) {
        this.urls = urls;
    }
}
