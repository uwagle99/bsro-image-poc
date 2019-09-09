import boto3
import os
import sys
import uuid
import logging
from PIL import Image
import PIL.Image


print('Test in AWS')
s3_client = boto3.client('s3')
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)


def resize_image(image_path, resized_path):
    image = Image.open(image_path)
    print('# Saving Resized image to destination bucket - ', resized_path)
    width = os.environ['width']
    height = os.environ['height']
    print('# required width of image ',width)
    print('# required height of image ',height)
    resizedImage = image.resize([int(width),int(height)],PIL.Image.ANTIALIAS)
    resizedImage.save(resized_path)


def handler(event, context):
    for record in event['Records']:
        bucket = record['s3']['bucket']['name']
        key = record['s3']['object']['key']
        download_path = '/tmp/{}-{}'.format(uuid.uuid4(), key)
        upload_path = '/tmp/resized-{}'.format(key)
        print('# download_path imported PIL Image and PIL', download_path)
        print('# upload_path', upload_path)
        print('# key is ', key)
        print('# bucket is ',bucket)
        s3_client.download_file(bucket, key, download_path)
        resize_image(download_path, upload_path)
        s3_client.upload_file(upload_path, bucket.replace('-source',''), key)
