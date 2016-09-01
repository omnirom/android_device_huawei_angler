# Inherit omni-specific board config
include device/huawei/angler/BoardConfigOmni.mk

# Inherit base AOSP device configuration
$(call inherit-product, device/huawei/angler/aosp_angler.mk)

# include vendor blobs
$(call inherit-product-if-exists, vendor/huawei/angler/angler-vendor.mk)

# Inherit APNs list
$(call inherit-product, vendor/omni/config/gsm.mk)

# Inherit from our custom product configuration
$(call inherit-product, vendor/omni/config/common.mk)

# Bootanimation
TARGET_BOOTANIMATION_SIZE := 1080x720

# Override product naming for Omni
PRODUCT_NAME := omni_angler
PRODUCT_BRAND := google
PRODUCT_MODEL := Nexus 6p
PRODUCT_MANUFACTURER := HUAWEI
PRODUCT_RESTRICT_VENDOR_FILES := false

PRODUCT_BUILD_PROP_OVERRIDES += \
    BUILD_FINGERPRINT="google/angler/angler:7.0/NRD90M/3085278:user/release-keys" \
    PRIVATE_BUILD_DESC="angler-user 7.0 NRD90M 3085278 release-keys"
