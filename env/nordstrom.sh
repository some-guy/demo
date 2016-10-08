#!/bin/bash
[[ -f /etc/default/hadoop-0.20 ]] && source /etc/default/hadoop-0.20

export TOOLS_HOME="/usr/local/tools"
export EC2_HOME=/usr/local/aws/ec2-api-tools
export EC2_AMI_HOME=/usr/local/aws/ec2-ami-tools


# use /usr/java/latest in ~/.bashrc for current version
export JAVA_HOME=/usr/java/default
export PATH=\
/usr/local/bin:\
/usr/bin:\
/usr/sbin:\
/bin:\
/sbin

unset command_not_found_handle

export HISTCONTROL=ignoreboth
export HISTIGNORE='history*'
shopt -s checkwinsize

alias ls='ls --color=auto'
alias lT='ls -alrt'
alias lS='ls -alrS'
alias ll='ls -alF'
alias la='ls -A'
alias l='ls -CF'

if `which vim > /dev/null` ; then
  alias vi='vim'
fi

[[ "$TERM" == "xterm" ]] && export TERM=xterm-256color

function parse_git_branch_and_add_brackets {
  git branch --no-color 2> /dev/null | sed -e '/^[^*]/d' -e 's/* \(.*\)/\ \[\1\]/'
}

export PS1="\[\e]0;\u@\h:\w\a\]\n\u@\h:\w\[\033[0;32m\]\$(parse_git_branch_and_add_brackets)\[\033[0m\] \t\n# "

umask 022

export EDITOR=vi
export VISUAL=vi
export PAGER=less
export LESS="-rnice"
export EC2_HOME=/usr/local/aws/ec2-api-tools
export EC2_AMI_HOME=/usr/local/aws/ec2-ami-tools

export LANG=en_US.utf8
export LC_ALL=en_US.utf8
export LC_COLLATE=en_US.utf8
