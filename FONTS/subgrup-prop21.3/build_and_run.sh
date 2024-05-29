if [ "$1" == "compile" ]; then
    ./gradlew jar
elif [ "$1" == "run" ]; then
    java -jar ./build/libs/gestor-de-teclats-1.0.jar
else
    echo "Usage: $0 {compile|run}"
fi