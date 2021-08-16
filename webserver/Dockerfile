FROM ubuntu:20.04

LABEL maintainer="joern@sprado.net"

ARG CONTAINER_TIMEZONE=Europe/Berlin

# Set timezone:
RUN ln -snf /usr/share/zoneinfo/$CONTAINER_TIMEZONE /etc/localtime && echo $CONTAINER_TIMEZONE > /etc/timezone

RUN echo "Install some additional packages" && \
	apt update -y && \ 
	apt install -y --no-install-recommends \
	locales \
	sudo \
	nano \
	build-essential \
	supervisor \
	screen \
	htop \
	openjdk-11-jre