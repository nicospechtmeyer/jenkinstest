FROM ubuntu:20.04

ARG CONTAINER_TIMEZONE=Europe/Berlin

# Set timezone:
RUN ln -snf /usr/share/zoneinfo/$CONTAINER_TIMEZONE /etc/localtime && echo $CONTAINER_TIMEZONE > /etc/timezone

RUN echo "Install some additional packages" && \
	apt update -y && \ 
	apt install -y --no-install-recommends \
	locales \
	sudo \
	nano \
	ssh \
	build-essential \
	supervisor \
	screen \
	htop \
	openjdk-11-jre

RUN echo "Enable SSH" && \
	systemctl ssh start && \
	systemctl ssh enable