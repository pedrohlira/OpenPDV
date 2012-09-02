#!/bin/bash
# Travar teclado e mouse no unix/linux
# autor: PhD - Systems Solutions
# descricao: Este comando faz o bloqueio/desbloqueio do teclado e mouse
# este arquivo deve ter permissao de execussao chmod +x ./travar.sh

# para descobrir qual inteiro representa seu teclado e mouse use o comando xinput list
teclado=6
mouse=7

if [ "$1" = "" ]; then
    echo "Passe como parametro [1 = ativar e 0 = desativar]."
    echo "Lembre-se de editar este arquivo para definir as variaveis de teclado e mouse de acordo com seu sistema"
else
    xinput set-int-prop $teclado "Device Enabled" 8 $1
    xinput set-int-prop $mouse "Device Enabled" 8 $1
fi

exit 0
