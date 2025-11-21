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
 
RUN pip install --upgrade pip

RUN pip install torch==2.6.0 torchvision==0.21.0 torchaudio==2.6.0 \
    --index-url https://download.pytorch.org/whl/cu124

RUN pip install \
    transformers \
    accelerate \
    datasets \
    sentencepiece \
    huggingface_hub \
    bitsandbytes \
    safetensors \
    peft \
    jupyter jupyterlab \
    uvicorn fastapi

RUN pip install xformers

WORKDIR /workspace

EXPOSE 8888

CMD ["bash", "-c", "jupyter lab --ip=0.0.0.0 --no-browser --allow-root"]