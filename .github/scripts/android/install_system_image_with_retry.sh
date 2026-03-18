#!/usr/bin/env bash
set -euo pipefail

system_image_package="${ANDROID_SYSTEM_IMAGE_PACKAGE:?}"
sdk_root="${ANDROID_SDK_ROOT:-${ANDROID_HOME:-}}"

if [[ -z "$sdk_root" ]]; then
  echo "ANDROID_SDK_ROOT or ANDROID_HOME must be set"
  exit 1
fi

cleanup_partial_downloads() {
  rm -rf "$HOME/.android/cache"/* || true
  rm -rf "$sdk_root"/.android/cache/* || true
  rm -rf "$sdk_root"/temp/* || true
}

attempt=1
while [[ "$attempt" -le 3 ]]; do
  echo "Installing Android system image ($system_image_package), attempt $attempt/3"
  if sdkmanager --install "$system_image_package" --channel=0; then
    exit 0
  fi

  echo "System image installation failed on attempt $attempt"
  cleanup_partial_downloads
  sleep 5
  attempt=$((attempt + 1))
done

echo "Failed to install Android system image after retries: $system_image_package"
exit 1
