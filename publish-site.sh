#!/usr/bin/env sh

set -e

mvn clean install
mvn site
mvn site-deploy
