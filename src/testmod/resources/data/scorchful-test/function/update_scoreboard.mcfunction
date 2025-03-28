
execute store result score Temperature Environment run thermoo environment temperature ~ ~ ~ celsius
execute store result score Humidity Environment run thermoo environment relativehumidity ~ ~ ~
execute store result score TemperatureChange Environment run thermoo environment temperature @s

# ensures that it is always at the top (makes it easier for me to track)
scoreboard players add TemperatureChange Environment 1000