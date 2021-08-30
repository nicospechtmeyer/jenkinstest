FROM jsprado/analytics-test-r:0.0.1

LABEL maintainer="joern@sprado.net"

# configure default locale, see https://github.com/rocker-org/rocker/issues/19 
RUN echo "de_DE.UTF-8 UTF-8" >> /etc/locale.gen && \
	locale-gen de_DE.utf8 && \
	/usr/sbin/update-locale LANG=de_DE.UTF-8

ENV LC_ALL de_DE.UTF-8 
ENV LANG de_DE.UTF-8
ENV PLATFORM_ENCRYPTION_KEY ""

RUN mkdir -p /var/run/sshd
RUN mkdir -p /var/log/supervisor

RUN mkdir -p /app
RUN mkdir -p /app/tmp
RUN mkdir -p /app/lib
RUN mkdir -p /app/tests
RUN mkdir -p /app/config

COPY ./lib /app/lib
COPY ./tests /app/tests
COPY ./assets /app/assets
COPY ./supervisor/supervisord.conf /etc/supervisor/supervisord.conf
COPY ./supervisor/MicroR.conf /etc/supervisor/conf.d/MicroR.conf

WORKDIR /app

EXPOSE 80

CMD ["/usr/bin/supervisord", "-c", "/etc/supervisor/supervisord.conf"]
