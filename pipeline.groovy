pipeline {
    agent any

    stages {
        stage('Подготовка workspace') {
            steps {
                cleanWs() // Безопасная очистка workspace
                echo "Workspace очищен"
            }
        }

        stage('Maven check') {
            steps {
                sh '''
    echo "=== ПРОВЕРКА НАЛИЧИЯ MAVEN ==="
    
    # Проверяем наличие maven
    if command -v mvn &> /dev/null; then
    echo "✅ Maven уже установлен"
    mvn --version
    else
    echo "❌ Maven не найден, устанавливаю..."
    apk update
    apk add maven
    echo "✅ Maven установлен"
    mvn --version
    fi
    '''
            }
        }

        stage('Клонирование репозитория') {
            steps {
                echo "Клонируем репозиторий..."

                // Используем встроенный checkout (самый безопасный способ)
                checkout([
                        $class           : 'GitSCM',
                        branches         : [[name: '*/master']],
                        extensions       : [],
                        userRemoteConfigs: [[
                                                    url          : 'https://github.com/anokhin-a-ig/online-goods-store.git',
                                                    credentialsId: ''
                                            ]]
                ])

                echo "✅ Репозиторий успешно клонирован"
            }
        }

        stage('Сборка проекта') {
            steps {
                echo "Начинаем сборку проекта"

                sh 'mvn clean package -DskipTests=true'
                echo "✅  Проект успешно собран"
            }
        }

    }
}