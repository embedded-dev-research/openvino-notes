#!/usr/bin/env bash
set -euo pipefail

mkdir -p "${HOME}/.android"
cat > "${HOME}/.android/advancedFeatures.ini" <<'EOF'
HVF = off
EOF
