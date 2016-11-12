# BIOS Pass Bypass Help

### Table of Contents

[1 - What is a BIOS Hash?](#a3)
[2 - What does BIOS Pass Bypass do?](#b)
[3 - What will I need?](#c)
[3.1 - How do I acquire my BIOS Hash?](#c1)
[3.1.1 - Images of BIOS Hashes on different systems](#c11)
[3.2 - Obtaining a password without a hash](#c2)

## <a name="a3">1 - What is a BIOS Hash?</a>

In some BIOSes, by default or after entering a wrong password for 3 times, a hash will be displayed, accompanying a message telling the user that the system has been locked. BIOS Pass Bypass can give you a working password using the displayed hash.

## <a name="b">2 - What does BIOS Pass Bypass do?</a>

BIOS restricts access to a computer using a password in some places, namely:

*   As soon as you turn on your computer.
*   When accessing the BIOS setup.

In these cases BIOS Pass Bypass can help you bypass these passwords on some systems (and not all).

## <a name="c">3 - What will I need?</a>

For old BIOSes with a master password, the only thing you need is the name of the manufacturer of the BIOS. In other cases, you have to obtain the BIOS hash.

### <a name="c1">3.1 - How do I acquire my BIOS Hash?</a>

You can obtain a BIOS hash, using one of these methods:

*   Visible in the password prompt screen.
*   Visible after entering 3 wrong passwords.
*   For some manufacturers (like Fujitsu) you have to enter a specific sequence of wrong passwords. For example:
    3hqgo3 jqw534 0qww294e

#### <a name="c11">3.1.1 - Images of BIOS Hashes on different systems</a>

Here you can see some instances of BIOS hashes, denoted by a red rectangle:
![BIOS Hash](01.jpg)
![BIOS Hash](02.jpg)
![BIOS Hash](03.jpg)
![BIOS Hash](04.jpg)
![BIOS Hash](05.jpg)
![BIOS Hash](06.jpg)
![BIOS Hash](07.jpg)

### <a name="c2">3.2 - Obtaining a password without a hash</a>

For old BIOSes that don't provide a hash, you can go to the "Password for hash-less BIOSes" section and after choosing a make and model, see the master passwords for that specific BIOS.

**Notice: If there were more than 1 password, try all of them, until you find the correct one.**

**Notice: In some cases, the algorithm used for obtaining a password from a hash is case sensitive. For example the passwords obtained for the following two hashes are different.
8WG0865-959B, 8wg0865-959b**