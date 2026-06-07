#!/bin/bash

echo "🛠️  Generando archivos con ANTLR..."

echo "🧹 Limpiando proyecto con Maven..."
mvn clean

echo "📦 Compilando proyecto con Maven..."
mvn package
java -jar target/demo-1.0-jar-with-dependencies.jar ejemplo.txt   

echo "✅ Todo compilado. Ejecuta con:"
