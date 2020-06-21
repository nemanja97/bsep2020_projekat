from time import sleep
from random import choice, randint
import sys
import platform
from datetime import datetime
    
def main():
    state = sys.argv[2]
    usernames = ['USERNAME-1111', 'USERNAME-9999', 'USERNAME-0000']
    paths = ['E:\\test.kk', 'C:\\Test\\Test1\\smh.exe']
    apps = ['Missile_launcher.exe', 'Defense_system.exe', 'BFG-9000.bat']

    while True:
        if state == 'normal':
            perform_normal_log(choice(usernames), choice(paths), choice(apps))
        else:
            mode = randint(0, 2)
            if mode == 0:
                perform_3_invalid_login(choice(usernames))
            elif mode == 1:
                perform_3_access_denied(choice(paths))
            else:
                perform_3_encryptions(choice(paths))
        sleep(3)

def perform_normal_log(username, path, app):
    facilities = ['KERN', 'USER', 'MAIL', 'DAEMON', 'AUTH',
     'SYSLOG', 'LPR', 'NEWS', 'UUCP', 
     'CRON', 'AUTHPRIV', 'FTP', 'NTP', 
     'SECURITY', 'CONSOLE', 'SOLARIS_CRON', 'LOCAL0', 
     'LOCAL1', 'LOCAL2', 'LOCAL3', 'LOCAL4', 
     'LOCAL5', 'LOCAL6', 'LOCAL7']

    severities = ['NOTICE', 'INFORMATIONAL', 'DEBUG']

    messages = [
        'Encrypted file {}'.format(path),
        'File {} accessed'.format(path),
        'Access denied to {}'.format(path),
        'Attempted to execute {}'.format(app),
        'Backup successful',
        'Backup failed',
        'Connection from 69.88.121.3',
        'Connection to 177.220.9.1',
        'Login {}'.format(username),
        'Logout {}'.format(username),
        'System update',
        'System update failed'
    ]

    date = datetime.utcnow()
    date_str = date.strftime('%Y-%m-%dT%H:%M:%SZ')
    facility = choice(facilities)
    severity = choice(severities)
    host_name = platform.uname()[1]
    messsage = choice(messages)
    date = datetime.utcnow()
    date_str = date.strftime('%Y-%m-%dT%H:%M:%SZ')
    full_str = '{} {} {} {} {}\n'.format(date_str, facility, severity, host_name, messsage)
    print(full_str)
    file = open(sys.argv[1], 'a')
    file.write(full_str)
    file.close()

def perform_3_invalid_login(username):
    date = datetime.utcnow()
    date_str = date.strftime('%Y-%m-%dT%H:%M:%SZ')
    facility = 'SECURITY'
    severity = 'ERROR'
    host_name = platform.uname()[1]
    messsage = 'Invalid credentials username: ' + username
    for _ in range (3):
        date = datetime.utcnow()
        date_str = date.strftime('%Y-%m-%dT%H:%M:%SZ')
        full_str = '{} {} {} {} {}\n'.format(date_str, facility, severity, host_name, messsage)
        print(full_str)
        file = open(sys.argv[1], 'a')
        file.write(full_str)
        file.close()
        sleep(3)

def perform_3_access_denied(path):
    date = datetime.utcnow()
    date_str = date.strftime('%Y-%m-%dT%H:%M:%SZ')
    facility = 'SECURITY'
    severity = 'WARNING'
    host_name = platform.uname()[1]
    messsage = 'Access denied to ' + path
    for _ in range (3):
        date = datetime.utcnow()
        date_str = date.strftime('%Y-%m-%dT%H:%M:%SZ')
        full_str = '{} {} {} {} {}\n'.format(date_str, facility, severity, host_name, messsage)
        print(full_str)
        file = open(sys.argv[1], 'a')
        file.write(full_str)
        file.close()
        sleep(3)

def perform_3_encryptions(path):
    date = datetime.utcnow()
    date_str = date.strftime('%Y-%m-%dT%H:%M:%SZ')
    facility = 'SECURITY'
    severity = 'INFORMATIONAL'
    host_name = platform.uname()[1]
    messsage = 'Encrypted file ' + path
    for _ in range (3):
        date = datetime.utcnow()
        date_str = date.strftime('%Y-%m-%dT%H:%M:%SZ')
        full_str = '{} {} {} {} {}\n'.format(date_str, facility, severity, host_name, messsage)
        file = open(sys.argv[1], 'a')
        file.write(full_str)
        file.close()
        sleep(3)

if __name__ == "__main__":
    main()
