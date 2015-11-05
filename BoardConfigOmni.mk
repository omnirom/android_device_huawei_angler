# Inline kernel
BOARD_KERNEL_IMAGE_NAME := Image.gz-dtb
TARGET_KERNEL_SOURCE := kernel/huawei/angler
TARGET_KERNEL_CONFIG := angler_defconfig

# Enable vendor image symlink
BOARD_NEEDS_VENDORIMAGE_SYMLINK := true
