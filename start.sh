#!/bin/bash
# =====================================
#   Starting Trainingsregister App
# =====================================
echo
# Kontrollera att Java är installerat
if ! command -v java &> /dev/null
then
  echo "[ERROR] Java is not installed or not in PATH."
  echo "Please install Java 17+ and try again."
  read -p "Press Enter to exit..."
  exit 1
fi
# Kontrollera att Maven Wrapper finns
if [ ! -f "./mvnw" ]; then
  echo "[ERROR] mvnw not found!"
  echo "Make sure you are in the project root folder."
  read -p "Press Enter to exit..."
  exit 1
fi
echo
echo "Starting Spring Boot application with embedded H2 database..."
echo "(This may take a few seconds)"
echo
# Starta browser (öppna localhost:8080 i standardwebbläsare)
if command -v xdg-open &> /dev/null; then
  xdg-open "http://localhost:8080" >/dev/null 2>&1 &
elif command -v open &> /dev/null; then
  open "http://localhost:8080" >/dev/null 2>&1 &
fi
echo
echo "======================================================="
echo " Application is starting..."
echo " Shut down application by pressing Ctrl+C."
echo "======================================================="
echo
# Kör Spring Boot via Maven Wrapper
./mvnw spring-boot:run
echo
echo "Application stopped."
read -p "Press Enter to exit..."