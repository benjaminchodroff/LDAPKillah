#!/bin/bash
### Created by Benjamin Chodroff
### benjamin.chodroff@gmail.com
### Designed to help load test an ldap server
### Make sure you have java 1.6 or later installed and JAVA_HOME properly set

package="LDAPKillah"
PROVIDERURL="ldaps://yourldapserver.com:636/"
LDAPBASE="dc=company,dc=com"
LDAPUSER="cn=ldapusername"
LDAPPASS="mypassword"
NUMTHREADS=1
INCREMENT=499
MAXUSERS=150000
TIMELIMIT=10000
DELAYTIME=1000

if [[ $# -eq 0 ]] ; then
    ./$0 -h
    exit 0
fi
while test $# -gt 0; do
        case "$1" in
                -h|--help)
                        echo "$package - attempt to kill an ldap server"
                        echo " "
                        echo "Created by Benjamin Chodroff"
						echo "benjamin.chodroff@gmail.com"
						echo "Designed to help load test an ldap server"
						echo "Make sure you have java 1.6 or later installed and JAVA_HOME properly set"
                        echo " "
                        echo "Defaults are provided below"
                        echo "$package [options]"
                        echo " "
                        echo "options:"
                        echo "-h, --help                        show this help"
                        echo "-H \"$PROVIDERURL\"        specify a ldap query string"
                        echo "-b \"$LDAPBASE\"               specify a ldap base"
                        echo "-D \"$LDAPUSER\"                 specify a bind user id"
                        echo "-w \"$LDAPPASS\"                   specify a bind password"
                        echo "--threads $NUMTHREADS                       specify a number of threads to kill with"
                        echo "--increment $INCREMENT                   specify how many users to search for in a single query"
                        echo "--maxusers $MAXUSERS                 specify the number of users to search up to"
                        echo "--timelimit $TIMELIMIT                 specify how long in ms to let the ldap server process per request"
                        echo "--delaytime $DELAYTIME                  specify how long in ms to wait between queries"
                        echo " "
                        exit 0
                        ;;
                -H)
                        shift
                        if test $# -gt 0; then
                        		export PROVIDERURL=$1
                        else
                        		echo "no provider url provided"
                        		exit 1
                        fi
                        shift
                        ;;
				-b)
                        shift
                        if test $# -gt 0; then
                        		export LDAPBASE=$1
                        else
                        		echo "no ldap base provided"
                        		exit 1
                        fi
                        shift
                        ;;
				-D)
                        shift
                        if test $# -gt 0; then
                        		export LDAPUSER=$1
                        else
                        		echo "no ldap user provided"
                        		exit 1
                        fi
                        shift
                        ;;
				-w)
                        shift
                        if test $# -gt 0; then
                        		export LDAPPASS=$1
                        else
                        		echo "no ldap pass provided"
                        		exit 1
                        fi
                        shift
                        ;;
				--threads)
                        shift
                        if test $# -gt 0; then
                        		export NUMTHREADS=$1
                        else
                        		echo "no number of threads provided"
                        		exit 1
                        fi
                        shift
                        ;;
				--increment)
                        shift
                        if test $# -gt 0; then
                        		export INCREMENT=$1
                        else
                        		echo "no increment number provided"
                        		exit 1
                        fi
                        shift
                        ;;
				--maxusers)
                        shift
                        if test $# -gt 0; then
                        		export MAXUSERS=$1
                        else
                        		echo "no max users provided"
                        		exit 1
                        fi
                        shift
                        ;;
				--timelimit)
                        shift
                        if test $# -gt 0; then
                        		export TIMELIMIT=$1
                        else
                        		echo "no ldap query time limit provided"
                        		exit 1
                        fi
                        shift
                        ;;
				--delaytime)
                        shift
                        if test $# -gt 0; then
                        		export DELAYTIME=$1
                        else
                        		echo "no delay time provided"
                        		exit 1
                        fi
                        shift
                        ;;
                        
                *)
                        break
                        ;;
        esac
done

java -jar LDAPKillah.jar $PROVIDERURL $LDAPBASE $LDAPUSER $LDAPPASS $NUMTHREADS $INCREMENT $MAXUSERS $TIMELIMIT $DELAYTIME
