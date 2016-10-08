# ~/.bashrc: executed by bash(1) for non-login shells.
# see /usr/share/doc/bash/examples/startup-files (in the package bash-doc)
# for examples

# If not running interactively, don't do anything
[ -z "$PS1" ] && return

HISTCONTROL=ignoredups

# append to the history file, don't overwrite it
shopt -s histappend

# for setting history length see HISTSIZE and HISTFILESIZE in bash(1)
HISTSIZE=1000
HISTFILESIZE=2000

# check the window size after each command and, if necessary,
# update the values of LINES and COLUMNS.
shopt -s checkwinsize

# make less more friendly for non-text input files, see lesspipe(1)
[ -x /usr/bin/lesspipe ] && eval "$(SHELL=/bin/sh lesspipe)"


#
#if [ -f /etc/profile.d/bigtip.sh ]; then
#    . /etc/profile.d/bigtip.sh
#fi

#
if [ -f ~/.bash_aliases ]; then
    . ~/.bash_aliases
fi

export HADOOP_HOME="/mnt0/home/mike/ws-hadoop-cdh/hadoop-0.20.2-cdh3u5"
export IRCNAME=m
export IRCNICK=some_guy
export IRCSERVER=irc.freenode.com
export IRCUSER=m
alias phpdebug='XDEBUG_CONFIG="idekey=xdebug-netbeans" php'
export PATH=$HADOOP_HOME/bin:$PATH
#export J2D_PIXMAPS=shared
#export _JAVA_OPTIONS="-Dsun.java2d.opengl=true"
#arch=x86_64
#fedver=fc17
#ver=3.4.0-0.rc4.git1.1.mike3
#export arch fedver ver
#export _JAVA_OPTIONS="-Dsun.java2d.opengl=true -Dapple.awt.graphics.UseQuartz=true -Dsun.zip.disableMemoryMapping=true -Dawt.useSystemAAFontSettings=on  -Dswing.aatext=true"
export WT_HOST=where.whotoo.com/app_dev.php
export WT_API_HOST=where.whotoo.com/app_dev.php
export WT_API_ID=3r6e4j34f00044o48o48s00sgoo48o4wwcgocgcgogoc0w80wk
export WT_API_SECRET=5c63c26xbf4sogw00kc808owcsowgcgwosos4k0c4gwssg8kws
