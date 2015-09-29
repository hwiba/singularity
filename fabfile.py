#-*- coding: utf-8 -*-

from fabric.operations import local
from fabric.api import run, env, execute
import time

env.git_clone = "https://github.com/hwiba/singularity.git"
git_clone_dir = "/root/gitCloneDir/target"
tomcat_dir = "/root/tomcat_dev/webapps/"

def nginxRestart() :
	local("/etc/init.d/nginx restart")

def tomcatStop() :
	local("sh /root/tomcat_dev/bin/shutdown.sh")

def tomcatStart() :
	local("sh /root/tomcat_dev/bin/startup.sh")

def backup() :
	now = time.localtime()
	backupDir = "/root/backupDeploy/"+str(now.tm_year)+"/"+str(now.tm_mon)+"/"+str(now.tm_mday)
	local("mkdir -p "+backupDir)
	local("cp -r "+tomcat_dir+"ROOT "+ backupDir)
	local("mv "+backupDir+"/ROOT "+backupDir+"/"+str(now.tm_hour)+"_"+str(now.tm_min)+"_"+str(now.tm_sec))

def rsync() :
	local("mv "+git_clone_dir+"/singularity-0.0.1-SNAPSHOT "+git_clone_dir+"/ROOT")
	local("rsync -avr --delete "+git_clone_dir+"/ROOT "+tomcat_dir)

def tempPageShow() :
	local("mv /etc/nginx/sites-enabled/webadv.com /etc/nginx/sites-available/")
	local("mv /etc/nginx/sites-available/testPage /etc/nginx/sites-enabled/")

def tempPageOut() :
	local("mv /etc/nginx/sites-enabled/testPage /etc/nginx/sites-available/")
	local("mv /etc/nginx/sites-available/webadv.com /etc/nginx/sites-enabled/")

def deploy() :
	#git clone 및 패키징은 종료된 상태.
	execute(backup)
	execute(tempPageShow)
	execute(nginxRestart)
	# 임시 페이지로 전환 완료
	execute(tomcatStop)
	execute(rsync)
	execute(tomcatStart)
	execute(tempPageOut)
	execute(nginxRestart)

