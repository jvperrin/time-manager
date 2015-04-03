Since the api is running in a subdirectory of the repository, a different command must be used than the regular push:

    git subtree push --prefix api heroku master

Found from http://stackoverflow.com/questions/18868769/deploy-git-subdirectory-on-heroku

