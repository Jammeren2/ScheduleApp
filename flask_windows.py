from flask import Flask, request
from flask_restful import Resource, Api
import pymssql
from flask import Response
import hashlib
from datetime import datetime

def custom_decode(data, key=5):
    m = hashlib.sha256()
    m.update(data.encode('utf-8'))
    data = m.hexdigest()
    decoded = ''.join([chr(ord(char) - key) for char in data])
    return decoded

app = Flask(__name__)
api = Api(app)
conn = pymssql.connect(server="217.71.129.139", user="Jammerenka2", password="Jammerenka2", database="institut2", charset='utf8', port=4408)

class Schedule(Resource):
    def post(self):

        data = request.get_json()
        param = data.get('param')
        decoded_data = custom_decode(param)
        if decoded_data == "]_2.00^_//1\\a\\4\\1.,\\\\4]0_/004]3_^`a],1+`-24\\300]32---`0a1,0a1^.`":
            group_name = data.get('group')
            teacher = data.get('teacher')
            week = data.get('week')
            room = data.get('room')
            day = data.get('day')

            where_clauses = []

            if group_name:
                where_clauses.append(f"g.name = '{group_name}'")
            if teacher:
                where_clauses.append(f"p.teacher_fio = '{teacher}'")
            if week:
                where_clauses.append(f"w.num = {week}")
            if room:
                where_clauses.append(f"r.name = '{room}'")
            if day:
                where_clauses.append(f"d.name = '{day}'")

            # Если есть хотя бы одно условие, добавляем WHERE
            where_statement = ""
            if where_clauses:
                where_statement = "WHERE " + " AND ".join(where_clauses)

            query = f"""
                SELECT 
                    g.name AS group_name, 
                    d.name AS day_name,
                    t.tiime AS time,
                    sub.num AS subject,
                    p.teacher_fio AS teacher,
                    STRING_AGG(r.name, ',') AS rooms,  -- Собираем кабинеты в одну строку
                    tp.name AS type
                FROM 
                    schedule s
                    JOIN GROOUP g ON s.GROOUP_id = g.id
                    JOIN days d ON s.day_id = d.id
                    JOIN tiimes t ON s.time_id = t.id
                    JOIN subjects sub ON s.para_id = sub.id
                    JOIN prepodi p ON s.prepod_id = p.id
                    JOIN schedule_rooms sr ON s.id = sr.id_schedule
                    JOIN rooms r ON sr.id_room = r.id
                    JOIN types tp ON s.para_type_id = tp.id
                    JOIN schedule_week sw ON s.id = sw.id_schedule
                    JOIN weeks w ON sw.id_week = w.num
                {where_statement}
                GROUP BY 
                    g.name, d.name, d.id, t.tiime, t.id, sub.num, p.teacher_fio, tp.name  -- Добавляем колонки в GROUP BY
                ORDER BY 
                    d.id, t.id;  -- Оставляем сортировку по id
            """

            try:
                cursor = conn.cursor(as_dict=True)
                cursor.execute(query)

                results = list(cursor)
                sch_list = []

                if results:
                    for row in results:
                        group_name = row['group_name'].encode('latin1').decode('windows-1251')
                        day_name = row['day_name'].encode('latin1').decode('windows-1251')
                        time = row['time'].encode('latin1').decode('windows-1251')
                        subject = row['subject'].encode('latin1').decode('windows-1251')
                        teacher = row['teacher'].encode('latin1').decode('windows-1251')
                        rooms = row['rooms'].encode('latin1').decode('windows-1251').split(',')  # Разделяем кабинеты по запятой
                        type = row['type'].encode('latin1').decode('windows-1251')

                        sch_list.append({
                            'group_name': group_name,
                            'day_name': day_name,
                            'time': time,
                            'subject': subject,
                            'teacher': teacher,
                            'rooms': rooms,  
                            'type': type
                        })
                    return sch_list
                else:
                    return {'message': 'No data found'}, 404

            except Exception as e:
                return {'error': str(e)}, 500
        else:
            return {'message': 'Invalid request'}, 400






class Group(Resource):
    def get(self):
        where_clauses = []
        
       
        # Если есть хотя бы одно условие, добавляем WHERE
        where_statement = ""
        if where_clauses:
            where_statement = "WHERE " + " AND ".join(where_clauses)

        query = f"""
SELECT * from GROOUP

        """

        try:
            cursor = conn.cursor(as_dict=True)
            cursor.execute(query)

            results = list(cursor)
            sch_list = []

            if results:
                for row in results:
                    id_ = row['id']
                    name = row['name'].encode('latin1').decode('windows-1251')
                    faculty = row['faculty'].encode('latin1').decode('windows-1251')


                    sch_list.append({
                        'id': id_,  
                        'name': name,
                        'faculty': faculty,
                    })
                return sch_list
            else:
                return {'message': 'No data found'}, 404

        except Exception as e:
            return {'error': str(e)}, 500


class Week(Resource):
    def get(self):
        query = f"""
        SELECT * from weeks
        """

        try:
            cursor = conn.cursor(as_dict=True)
            cursor.execute(query)

            results = list(cursor)
            sch_list = []

            if results:
                for row in results:
                    num = row['num']
                    date_start = row['date_start'].strftime('%Y-%m-%d')  # Convert date to string
                    date_end = row['date_end'].strftime('%Y-%m-%d')  # Convert date to string

                    sch_list.append({
                        'num': num,  
                        'date_start': date_start,
                        'date_end': date_end,
                    })
                return sch_list
            else:
                return {'message': 'No data found'}, 404

        except Exception as e:
            return {'error': str(e)}, 500

    
class Prepod(Resource):
    def get(self):
        teacher_fio = request.args.get('fio')

        # Формируем SQL-запрос с полученным значением
        query = f"""
        SELECT * from prepodi where teacher_fio = '{teacher_fio}'
        """

        try:
            cursor = conn.cursor(as_dict=True)
            cursor.execute(query)

            results = list(cursor)
            sch_list = []

            if results:
                for row in results:
                    id_ = row['id']
                    teacher = row['teacher'].encode('latin1').decode('windows-1251')
                    teacher_fio = row['teacher_fio'].encode('latin1').decode('windows-1251')
                    teacher_link = row['teacher_link'].encode('latin1').decode('windows-1251')
                    if row['teacher_img'] == None:
                        teacher_img = row['teacher_img']
                    else:
                        teacher_img = row['teacher_img'].encode('latin1').decode('windows-1251')
                    teacher_mail = row['teacher_mail'].encode('latin1').decode('windows-1251')
                    teacher_phone = row['teacher_phone'].encode('latin1').decode('windows-1251')
                    teacher_adress = row['teacher_adress'].encode('latin1').decode('windows-1251')
                    sch_list.append({
                        'teacher': teacher,  
                        'teacher_fio': teacher_fio,
                        'teacher_link': teacher_link,
                        'teacher_img': teacher_img,
                        'teacher_mail': teacher_mail,
                        'teacher_phone': teacher_phone,
                        'teacher_adress': teacher_adress
                    })
                return sch_list
            else:
                return {'message': 'No data found'}, 404

        except Exception as e:
            return {'error': str(e)}, 500

api.add_resource(Prepod, '/prepod')
api.add_resource(Schedule, '/schedule')
api.add_resource(Group, '/group')
api.add_resource(Week, '/week')

if __name__ == "__main__":
    app.run(
        debug=True, 
        host='0.0.0.0', 
        port=62124
    )































def custom_decode(data, key=5):
    decoded = ''.join([chr(ord(char) - key) for char in data])
    return decoded