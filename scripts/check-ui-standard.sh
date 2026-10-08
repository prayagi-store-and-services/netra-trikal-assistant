#!/usr/bin/env bash
# Family UI standard check: header with app name + version + date/time, scrollable body, footer.
set -e
f=app/src/main/java/com/prayagi/netraassistant/ui/AppShell.kt
grep -q 'topBar' "$f" || { echo "missing fixed header (topBar)"; exit 1; }
grep -q 'bottomBar' "$f" || { echo "missing fixed footer (bottomBar)"; exit 1; }
grep -q 'verticalScroll' "$f" || { echo "missing scrollable body"; exit 1; }
grep -q 'BuildConfigInfo.VERSION' "$f" || { echo "header must show version"; exit 1; }
grep -q 'SimpleDateFormat' "$f" || { echo "header must show date/time"; exit 1; }
echo "UI standard OK"
