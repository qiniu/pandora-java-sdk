#!/usr/bin/env bash

#set -x

if [ -z ${PROJECT_DIR+x} ]; then
  echo "PROJECT_DIR is unset, use $(pwd)"
  PROJECT_DIR=$(pwd)
else
  echo "PROJECT_DIR is set to ${PROJECT_DIR}}"
fi

if [ -z ${NODE_COMMAND+x} ]; then
  echo "NODE_COMMAND is unset, use yarn"
  NODE_COMMAND="yarn"
else
  echo "NODE_COMMAND is set to ${NODE_COMMAND}"
fi

PROJECT_NAME=$(basename "${PROJECT_DIR}")
PROJECT_PARENT_DIR=$(dirname "${PROJECT_DIR}")

usage() {
  echo "./run.sh [help|init_local|update_deps|package_deps|unittest|package|all|clean|docker_build]"
  echo
  echo "Usage:"
  echo "    help: output this information"
  echo "    init_local: install dependencies in requirements.txt"
  echo "    update_deps: freeze dependencies from pip to requirements.txt"
  echo "    package_deps: package dependencies from pip into bins/libs directory"
  echo "    unittest: run unittest in bins/tests directory"
  echo "    package: package all file into archive file"
  echo "    all: run all process"
  echo "    docker_build: package in docker environment"
  echo "    clean: clean all build files"
}

init_local() {
  mkdir -p "${PROJECT_DIR}"/bins
  mkdir -p "${PROJECT_DIR}"/bins/libs
  mkdir -p "${PROJECT_DIR}"/bins/tests
  touch "${PROJECT_DIR}"/bins/__init__.py
  touch "${PROJECT_DIR}"/bins/tests/__init__.py
  pip install -r "${PROJECT_DIR}"/requirements.txt
}

update_deps() {
  pip freeze >"${PROJECT_DIR}"/requirements.txt
}

package_deps() {
  grep -v -f env.ignore.python3 "${PROJECT_DIR}"/requirements.txt >"${PROJECT_DIR}"/.tmp_requirements
  cat "${PROJECT_DIR}"/.tmp_requirements
  pip install -t "${PROJECT_DIR}"/bins/libs -r "${PROJECT_DIR}"/.tmp_requirements --no-deps --force-reinstall
  rm "${PROJECT_DIR}"/.tmp_requirements
}

unittest() {
  pytest "${PROJECT_DIR}"/bins/tests/*
}

build_webapp() {
  if [ -d develop ]; then
    cd "${PROJECT_DIR}"/develop && ${NODE_COMMAND} install && ${NODE_COMMAND} run build && cd "${PROJECT_DIR}" || exit
  fi
}

move_resource(){
    cp -Rf "${PROJECT_DIR}"/develop/lib/** "${PROJECT_DIR}"/appserver/static/src_trigger
}

package() {
  build_webapp
  move_resource
  export COPY_EXTENDED_ATTRIBUTES_DISABLE=true
  export COPYFILE_DISABLE=true
  default_tar_directory=$(cd .. && pwd)
  default_app_name=$(basename `pwd`)
  default_build_file_full_path="$default_tar_directory/$default_app_name.tar.gz"

  tar_directory=${ENV_TAR_DIRECTORY:-$default_tar_directory}
  build_file_full_path=${ENV_BUILD_FILE_FULL_PATH:-$default_build_file_full_path}
  app_name=${ENV_APP_NAME:-$default_app_name}
  cd "${PROJECT_PARENT_DIR}" || exit

  if [ $ENV_TAR_DIRECTORY ]
  then
    tar -C $tar_directory -czvf $build_file_full_path --exclude=$app_name/develop --exclude="venv"  $app_name
  else
    tar czf "${PROJECT_NAME}".tar.gz --exclude="venv" --exclude="develop" --exclude=".git" --exclude=".DS_Store" "${PROJECT_NAME}" || exit
    mkdir "${PROJECT_DIR}"/dist && mv "${PROJECT_NAME}".tar.gz "${PROJECT_DIR}"/dist
  fi
}

docker_build() {
  clean
  docker build --rm -t custom_input .
  docker run --rm=true -v $(PWD):/usr/src/custom_input -it custom_input
}

clean() {
  rm -r "${PROJECT_DIR}"/.pytest_cache
  rm -r "${PROJECT_DIR}"/bins/libs/*
  rm -r "${PROJECT_DIR}"/dist
}

upload() {
  # shellcheck disable=SC2045
  # shellcheck disable=SC2045
  if [ -z ${PANDORA_HOST+x} ]; then
    echo "PANDORA_HOST is unset!"
    exit 1
  fi
  if [ -z ${PANDORA_TOKEN+x} ]; then
    echo "PANDORA_TOKEN is unset!"
    exit 1
  fi
  if [ -z ${PANDORA_PORT+x} ]; then
    echo "PANDORA_PORT is unset!"
    exit 1
  fi
  if [ -z ${PANDORA_SCHEME+x} ]; then
    echo "PANDORA_SCHEME is unset!"
    exit 1
  fi
  for file in dist/*.tar.gz; do
    upload_app "$file" --host "${PANDORA_HOST}" --scheme "${PANDORA_SCHEME}" --port "${PANDORA_PORT}" --token "${PANDORA_TOKEN}" --overwrite
  done
}

#[all|help|init_local|update_deps|package_deps|unittest|package|clean|docker_build]

ACTION=$1
if [ -z ${ACTION+x} ]; then
  echo "ACTION is not set"
  help_usage
else
  case ${ACTION} in
  help)
    usage
    ;;
  init_local)
    init_local
    ;;
  update_deps)
    update_deps
    ;;
  package_deps)
    package_deps
    ;;
  unittest)
    unittest
    ;;
  package)
    clean
    package_deps
    package
    ;;
  all)
    clean
    init_local
    package_deps
    unittest
    package
    ;;
  docker_build)
    docker_build
    ;;
  upload)
    upload
    ;;
  clean)
    clean
    ;;
  *)
    echo "Sorry, I don't understand"
    usage
    ;;
  esac
fi
