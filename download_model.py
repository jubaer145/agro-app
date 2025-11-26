#!/usr/bin/env python3
"""
Download a pre-trained plant disease detection TFLite model
"""

import urllib.request
import os

def download_model():
    """Download a working plant disease detection model"""
    
    model_path = "app/src/main/assets/plant_disease_model.tflite"
    
    # Try multiple sources
    sources = [
        # TensorFlow Hub - Cassava Disease Detection (works!)
        "https://tfhub.dev/google/lite-model/cropnet/classifier/cassava_disease_V1/1?lite-format=tflite",
        # Alternative: Plant pathology model
        "https://github.com/rajeshkanna1999/Plant_Disease_Detection/raw/main/model/plant_disease_model.tflite",
        # Alternative: PlantVillage model
        "https://storage.googleapis.com/download.tensorflow.org/models/tflite/aiy/2017_12_18/plant_classification.tflite"
    ]
    
    for idx, url in enumerate(sources):
        print(f"Trying source {idx + 1}/{len(sources)}: {url[:60]}...")
        try:
            urllib.request.urlretrieve(url, model_path)
            size = os.path.getsize(model_path)
            if size > 1000:  # At least 1KB
                print(f"✓ Successfully downloaded model ({size / 1024 / 1024:.2f} MB)")
                return True
        except Exception as e:
            print(f"✗ Failed: {e}")
            continue
    
    print("❌ Could not download model from any source")
    return False

if __name__ == "__main__":
    download_model()
