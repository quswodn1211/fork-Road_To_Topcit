FROM nvidia/cuda:12.4.1-devel-ubuntu22.04

ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update && apt-get install -y \
    python3 python3-pip python3-venv \
    git wget curl vim \
    build-essential \
    && apt-get clean && rm -rf /var/lib/apt/lists/*

RUN ln -s /usr/bin/python3 /usr/bin/python

RUN apt-get update && apt-get install -y locales \
    && locale-gen en_US.UTF-8

ENV LANG=en_US.UTF-8
ENV LC_ALL=en_US.UTF-8
 
COPY requirements.txt /workspace/requirements.txt

RUN pip install --upgrade pip
RUN pip install -r /workspace/requirements.txt

COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh
ENTRYPOINT ["/entrypoint.sh"]

WORKDIR /workspace
COPY . /workspace

EXPOSE 8888

CMD ["bash", "-c", "jupyter lab --ip=0.0.0.0 --no-browser --allow-root --ServerApp.password=$JUPYTER_PASSWORD"]
>>>>>>> 4916b0a (workflow added)
