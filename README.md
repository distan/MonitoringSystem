# MonitoringSystem
A monitoring client

本程序需要在Java环境下额外添加RxTx库

主要传感器基于CC2530单片机，利用ZigBee自组网汇总数据，然后通过USB转串口使电脑与协调器相连读取数据并存入数据库，使用了有温度、PH、浊度传感器
