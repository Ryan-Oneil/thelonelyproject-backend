# The Lonely Project - Backend

A social media built for connecting users based off similar traits entered on a user's profile

[Frontend project](https://lonelyproject.org/).

## Technologies Used

* Spring boot framework
* Liquibase
* PostgreSQL
* Firebase 
* Backblaze
* Websockets

## Live demo

Live site deployed on Cloudflare pages [https://lonelyproject.org](https://github.com/Ryan-Oneil/thelonelyproject).

### Running locally

docker pull ghcr.io/ryan-oneil/thelonelyproject-backend:master
docker run -d --name=lonelyproject-backend -p 80:8080 ghcr.io/ryan-oneil/thelonelyproject-backend:master

#### Environment Variables
spring_profiles_active=local

GOOGLE_APPLICATION_CREDENTIALS=firebase_config.json

backblaze.authToken=

backblaze.appId=

backblaze.appKey=
