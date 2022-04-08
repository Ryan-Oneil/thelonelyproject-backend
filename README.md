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

Live site deployed on Cloudflare
pages [https://lonelyproject.org](https://github.com/Ryan-Oneil/thelonelyproject).

### Running locally

If you don't have one already you'll need a Firebase account and project
setup [https://firebase.google.com/](https://firebase.google.com/)

You will also need a free account from Back
Blaze [https://www.backblaze.com/b2/cloud-storage.html](https://www.backblaze.com/b2/cloud-storage.html)

docker pull ghcr.io/ryan-oneil/thelonelyproject-backend:master

docker run -d --name=lonelyproject-backend -p 80:8080 -v /home/ryan/config/lonelyproject:/config \
-e GOOGLE_APPLICATION_CREDENTIALS='/config/firebase_config.json' \
-e backblaze.authToken='' \
-e backblaze.appId='' \
-e backblaze.appKey='' \
ghcr.io/ryan-oneil/thelonelyproject-backend:master

#### Environment Variables

spring_profiles_active=local

GOOGLE_APPLICATION_CREDENTIALS=firebase_config.json

backblaze.authToken=

backblaze.appId=

backblaze.appKey=
