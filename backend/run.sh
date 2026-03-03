#!/usr/bin/env bash
set -euo pipefail
ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"
OUT_DIR="$ROOT_DIR/out"
rm -rf "$OUT_DIR"
mkdir -p "$OUT_DIR"
find "$ROOT_DIR/src/main/java" -name '*.java' > "$OUT_DIR/sources.list"
javac -d "$OUT_DIR" @"$OUT_DIR/sources.list"
java -cp "$OUT_DIR" com.example.zhihu.backend.BackendServer
