#!/usr/bin/env bash
set -euo pipefail

android_config_dir="${HOME}/.android"
mkdir -p "${android_config_dir}"

cat > "${android_config_dir}/advancedFeatures.ini" <<'EOF'
HVF = off
EOF
