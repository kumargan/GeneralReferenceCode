FROM python:2.7-slim

WORKDIR /app

RUN echo "deb http://ftp.cn.debian.org/debian sid main" >> /etc/apt/sources.list && \ 
    apt-get update &&  \
    apt-get install -y tesseract-ocr libtesseract-dev libleptonica-dev libsm6 libxext6 gcc

COPY . .

CMD ["./start_app.sh"]

