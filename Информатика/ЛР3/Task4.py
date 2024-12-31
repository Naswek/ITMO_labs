import json
from json2xml import json2xml
from json2xml.utils import readfromjson

data = readfromjson('timetable.json')

print(json2xml.Json2xml(data).to_xml())


