FROM r-base:3.6.3

LABEL maintainer="joern@sprado.net"

ARG CONTAINER_TIMEZONE=Europe/Berlin

# install packages
RUN echo "Install texlive-full" && \
	apt-get update && \
	DEBIAN_FRONTEND=noninteractive apt-get install -y texlive-full && \
	apt-get clean && \
	rm -rf /var/lib/apt-get/lists/* /tmp/* /var/tmp/*

RUN echo "Install some additional packages" && \
	apt-get update -y && \
	apt-get install -y --no-install-recommends \
	locales \
	sudo \
	nano \
	build-essential \
	supervisor \
	screen \
	htop \
	openjdk-11-jre \
	cifs-utils \
	psmisc \
	libapparmor1 \
	libedit2 \
	zlib1g-dev \
	libssl-dev \
	pandoc \
	libgit2-dev \
	libxml2-dev

RUN echo "Install prerequisites pdftools" && \
	apt-get update -y && \
	apt-get install -y --no-install-recommends libtesseract-dev libleptonica-dev libwebp-dev libpoppler-cpp-dev

RUN echo "Install prerequisites http" && \
 	apt-get update -y && \
	apt-get install -y --no-install-recommends libcurl4-openssl-dev

RUN echo "Install additional R-packages" && \
	Rscript -e 'install.packages("ggplot2", dependencies = TRUE)' && \
	Rscript -e 'install.packages("rjson", dependencies = TRUE)' && \
	Rscript -e 'install.packages("tm", dependencies = TRUE)' && \
	Rscript -e 'install.packages("SnowballC", dependencies = TRUE)' && \
	Rscript -e 'install.packages("wordcloud", dependencies = TRUE)' && \
	Rscript -e 'install.packages("RColorBrewer", dependencies = TRUE)' && \
	Rscript -e 'install.packages("RCurl", dependencies = TRUE)' && \
	Rscript -e 'install.packages("XML", dependencies = TRUE)' && \
	Rscript -e 'install.packages("caret", dependencies = TRUE)' && \
	Rscript -e 'install.packages("mlbench", dependencies = TRUE)' && \
	Rscript -e 'install.packages("tau", dependencies = TRUE)' && \
	Rscript -e 'install.packages("data.table", dependencies = TRUE)' && \
	Rscript -e 'install.packages("data.frame", dependencies = TRUE)' && \
	Rscript -e 'install.packages("readxl", dependencies = TRUE)' && \
	Rscript -e 'install.packages("ssh", dependencies = TRUE)' && \
	Rscript -e 'install.packages("data.frame", dependencies = TRUE)' && \
	Rscript -e 'install.packages("png", dependencies = TRUE)' && \	
	Rscript -e 'install.packages("qpdf", dependencies = TRUE)' && \	
	Rscript -e 'install.packages("pdftools", dependencies = TRUE)'  && \
	Rscript -e 'install.packages("sqldf", dependencies = TRUE)' && \
	Rscript -e 'install.packages("telegram.bot", dependencies = TRUE)'

RUN echo "Install additional R-packages" && \
	Rscript -e 'install.packages("ggplot2", dependencies = TRUE)' && \
	Rscript -e 'install.packages("rjson", dependencies = TRUE)' && \
	Rscript -e 'install.packages("SnowballC", dependencies = TRUE)' && \
	Rscript -e 'install.packages("wordcloud", dependencies = TRUE)' && \
	Rscript -e 'install.packages("RColorBrewer", dependencies = TRUE)' && \
	Rscript -e 'install.packages("RCurl", dependencies = TRUE)' && \
	Rscript -e 'install.packages("XML", dependencies = TRUE)' && \
	Rscript -e 'install.packages("data.table", dependencies = TRUE)' && \
	Rscript -e 'install.packages("data.frame", dependencies = TRUE)' && \
	Rscript -e 'install.packages("readxl", dependencies = TRUE)' && \
	Rscript -e 'install.packages("pdftools", dependencies = TRUE)' && \
    Rscript -e 'install.packages("xtable", dependencies = TRUE)' && \
    Rscript -e 'install.packages("sqldf", dependencies = TRUE)' && \
	Rscript -e 'install.packages("telegram.bot", dependencies = TRUE)'

# set timezone:
RUN ln -snf /usr/share/zoneinfo/$CONTAINER_TIMEZONE /etc/localtime && echo $CONTAINER_TIMEZONE > /etc/timezone

# remove apt-get files
RUN apt-get clean && rm -rf /var/lib/apt-get/lists/* /tmp/* /var/tmp/*