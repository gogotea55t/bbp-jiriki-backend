if [ $# ne 1 ]; then
  echo 'タグを指定すべし'
  exit 1
fi

git pull
BBP_BACKEND_VERSION=$1 docker-compose pull
BBP_BACKEND_VERSION=$1 docker-compose up -d