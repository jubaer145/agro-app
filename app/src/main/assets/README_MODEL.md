# TensorFlow Lite Model Placeholder

## Model File: plant_disease_model.tflite

This directory should contain the actual TFLite model file.

### Where to get the model:

#### Option 1: Pre-trained PlantVillage Model
Download from: https://github.com/spMohanty/PlantVillage-Dataset
- Look for converted TFLite models
- Typical size: 5-15 MB
- 38 disease classes across 14 crop species

#### Option 2: TensorFlow Hub
https://tfhub.dev/google/collections/plant/1
- Pre-trained models ready to use
- Already in TFLite format

#### Option 3: Train Your Own Model
```python
# Train custom model for Kyrgyzstan crops
import tensorflow as tf
from tensorflow import keras

# Load PlantVillage dataset or collect local images
# Train MobileNetV2 or EfficientNet
# Export to TFLite

converter = tf.lite.TFLiteConverter.from_keras_model(model)
converter.optimizations = [tf.lite.Optimize.DEFAULT]
tflite_model = converter.convert()

with open('plant_disease_model.tflite', 'wb') as f:
    f.write(tflite_model)
```

#### Option 4: Download Sample Model
For testing, you can use this sample:
```bash
wget https://storage.googleapis.com/download.tensorflow.org/models/tflite/plant_disease_model.tflite
```

### Model Requirements:
- **Input**: 224x224 RGB image (float32, normalized 0-1)
- **Output**: Probability distribution over disease classes
- **Format**: TensorFlow Lite (.tflite)
- **Size**: Preferably < 20 MB for mobile deployment

### Placement:
Place the `.tflite` file in this directory:
```
app/src/main/assets/plant_disease_model.tflite
```

### Current Behavior:
Without the model file, the app will:
1. Try to load the model
2. Fall back to mock/demo inference if not found
3. Still provide functional testing experience
4. Log warnings about missing model

### To Add Your Model:
1. Place `plant_disease_model.tflite` in this folder
2. Ensure `labels.txt` matches your model's output classes
3. Rebuild the app
4. The detector will automatically use the real model

---

**Note**: The app is currently configured to work without the model file for development and testing purposes. Once you add a real model, disease detection will use actual AI inference.
