#!/usr/bin/env bash
set -euo pipefail

results_root_dir="${RESULTS_ROOT_DIR:-android-test-results}"
emulator_suite_name="${EMULATOR_SUITE_NAME:-androidTest}"

mapfile -t raw_files < <(find "$results_root_dir" -name 'instrumentation-raw.txt' | sort)
if [[ "${#raw_files[@]}" -eq 0 ]]; then
  echo "No instrumentation-raw.txt files found in $results_root_dir"
  exit 1
fi

for raw_file in "${raw_files[@]}"; do
  result_dir="$(dirname "$raw_file")"
  build_name="$(basename "$result_dir")"
  python3 .github/scripts/android/convert_instrumentation_to_junit.py \
    "$raw_file" \
    "$result_dir/instrumentation-results.xml" \
    "$result_dir/instrumentation.txt" \
    "${emulator_suite_name}-${build_name}"
done
