#!/bin/bash

TARGET_FOLDER=target
SOURCE_FOLDER=src/main/java
PROJECT_FOLDER=com/gabrieljesus/backend_file_processing

mkdir -p ${TARGET_FOLDER}/${PROJECT_FOLDER}
javac -d ${TARGET_FOLDER} ${SOURCE_FOLDER}/${PROJECT_FOLDER}/EZJsonField.java
javac -cp ${TARGET_FOLDER} -d ${TARGET_FOLDER} ${SOURCE_FOLDER}/${PROJECT_FOLDER}/Record.java
javac -cp ${TARGET_FOLDER} -d ${TARGET_FOLDER} ${SOURCE_FOLDER}/${PROJECT_FOLDER}/Payment.java
javac -cp ${TARGET_FOLDER} -d ${TARGET_FOLDER} ${SOURCE_FOLDER}/${PROJECT_FOLDER}/Receipt.java
javac -cp ${TARGET_FOLDER} -d ${TARGET_FOLDER} ${SOURCE_FOLDER}/${PROJECT_FOLDER}/BankStatement.java
javac -cp ${TARGET_FOLDER} -d ${TARGET_FOLDER} ${SOURCE_FOLDER}/${PROJECT_FOLDER}/utils/jsonparser/JsonParser.java
javac -cp ${TARGET_FOLDER} -d ${TARGET_FOLDER} ${SOURCE_FOLDER}/${PROJECT_FOLDER}/utils/Report.java
javac -cp ${TARGET_FOLDER} -d ${TARGET_FOLDER} ${SOURCE_FOLDER}/${PROJECT_FOLDER}/utils/Utils.java
javac -cp ${TARGET_FOLDER} -d ${TARGET_FOLDER} ${SOURCE_FOLDER}/${PROJECT_FOLDER}/App.java

mkdir ${TARGET_FOLDER}/resources
cp ${SOURCE_FOLDER}/resources/db.json ${TARGET_FOLDER}/resources
