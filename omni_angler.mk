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

# Allow tethering without provisioning app
PRODUCT_PROPERTY_OVERRIDES += \
    net.tethering.noprovisioning=true

# Camera
PRODUCT_PROPERTY_OVERRIDES += \
    persist.camera.HAL3.enabled=1 \
    persist.camera.cpp.duplication=false

# SELinux
PRODUCT_PROPERTY_OVERRIDES += \
    ro.build.selinux=1

# drmservice prop
PRODUCT_PROPERTY_OVERRIDES += \
    drm.service.enabled=true

# Snapdragon Camera
PRODUCT_PACKAGES += \
    SnapdragonCamera

# Override product naming for Omni
PRODUCT_NAME := omni_angler
PRODUCT_BRAND := google
PRODUCT_MODEL := Nexus 6p
PRODUCT_MANUFACTURER := Huawei
PRODUCT_RESTRICT_VENDOR_FILES := false

PRODUCT_BUILD_PROP_OVERRIDES += \
    BUILD_FINGERPRINT="google/angler/angler:7.0/NRD90M/3085278:user/release-keys" \
    PRIVATE_BUILD_DESC="angler-user 7.0 NRD90M 3085278 release-keys"
