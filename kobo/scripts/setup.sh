#!/usr/bin/env bash

# scripts/00_vagrant_up.sh


set -e

# ============================
# EXTEND ENVIRONMENT VARIABLES
if [ -d /home/vagrant ]; then
    # For Vagrant installations.
    SCRIPT_DIR=/home/vagrant/scripts
else
    # Non-Vagrant.
    THIS_SCRIPT_PATH=$(readlink -f "$0")
    SCRIPT_DIR=$(dirname "$THIS_SCRIPT_PATH")
fi

echo "Step 0 SCRIPT_DIR set to "  $SCRIPT_DIR " with step " $1

. $SCRIPT_DIR/01_environment_vars.sh
# ============================

# Install 'apt' repository keys if not already done.
[ -f $HOME_VAGRANT/.mark_keys_added ] || {
echo "Step 2 running 02_installation_keys..."
    sudo -u root  "$V_S/02_installation_keys.sh"
}

if [ $1 -eq 4 ];then
   echo "Step 3 running 03_apt_installs..."
   sudo -u root       "$V_S/03_apt_installs.sh"
fi

if [ $1 -eq 5 ];then
   echo "Step 4 running 04_postgis_extensions.sh..."
   sudo -u root       "$V_S/04_postgis_extensions.sh"
fi

if [ $1 -eq 6 ];then
  # Idempotently ensure that PostgreSQL is running.
  echo "....Step 5 starting postgresql ..."
  sudo service postgresql start
fi

if [ $1 -eq 7 ];then
  echo "....Step 6 creating 05_create_database.sh..."
  sudo -u postgres     "$V_S/05_create_database.sh"
fi

# KoBoCat:
if [ $1 -eq 8 ];then
   echo "....Step 7 running kc_10_virtualenvs..."
   sudo -u root      "$V_S/kc_10_virtualenvs.bash"
fi

if [ $1 -eq 9 ];then
   echo "....Step 8 running kc_20_clone_code..."
   sudo -u root      "$V_S/kc_20_clone_code.sh"
fi

if [ $1 -eq 10 ];then
   echo "....Step 9 running install_pip_requirements..."
   sudo -u root     "$V_S/kc_30_install_pip_requirements.bash"
fi

if [ $1 -eq 11 ];then
   echo "....Step 10 running npm_installs..."
   sudo -u root     "$V_S/kc_40_npm_installs.sh"
fi

if [ $1 -eq 12 ];then
   echo "....Step 11 running kc_50_migrate_db..."
   sudo -u root    "$V_S/kc_50_migrate_db.bash"
fi

if [ $1 -eq 13 ];then
   echo "....Step 12 running kc_60_environment_setup..."
   sudo -u root   "$V_S/kc_60_environment_setup.bash"
fi
echo "....Congrats KOBOCAT IS DONE...."

# KoBoForm:
if [ $1 -eq 14 ];then
echo "....Step 13 running kf_10_virtualenvs..."
sudo -u root     "$V_S/kf_10_virtualenvs.bash"
fi

if [ $1 -eq 15 ];then
echo "....Step 14 running kf_20_clone_code.sh..."
sudo -u root     "$V_S/kf_20_clone_code.sh"
fi

if [ $1 -eq 16 ];then
echo "....Step 15 running install_pip_requirements..."
sudo -u root     "$V_S/kf_30_install_pip_requirements.bash"
fi

if [ $1 -eq 17 ];then
echo "....Step 16 running kf_40_npm_installs ..."
sudo -u root     "$V_S/kf_40_npm_installs.sh"
fi

if [ $1 -eq 18 ];then
echo "....Step 17 running kf_50_migrate_db..."
sudo -u root     "$V_S/kf_50_migrate_db.bash"
fi

echo "....Congrats KOBOFORM IS DONE...."

# Enketo Express:
if [ $1 -eq 20 ];then
 echo "....Step 19 Starting Enketo ..."
 sudo -u root    "$V_S/enketo_install.old.bash"
fi

if [ $1 -eq 19 ];then
  echo "....Step 18 Trying to start server and adding it to cron job ..."
  sudo -u root   "$V_S/09_add_cronjobs.sh"
fi
echo "Done with installation"
echo "Dont forget to run grunt build_css as KOBOFORM SRC directory"
