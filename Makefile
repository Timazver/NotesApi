SHELL := /bin/bash

.PHONY: run test sync r t s

run r:
	./gradlew bootRun

test t:
	./gradlew test

sync s:
	./gradlew dependencies --refresh-dependencies
