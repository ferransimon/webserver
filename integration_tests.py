import unittest
import requests

class TestRestAPI(unittest.TestCase):

    __URL = "http://localhost:8080"
    __AUTH_ADMIN = ("admin", "admin")
    __AUTH_BASIC_USER = ("user1", "password")

    def test_get_list_of_users(self):
        r = requests.get(self.__URL+"/api/users", auth=self.__AUTH_ADMIN)
        resp = r.json()
        self.assertEquals(r.status_code, 200)
        self.assertTrue(isinstance(resp, list))

    def test_create_user_and_delete(self):
        new_user = {
            "username": "ferran",
            "password": "test",
            "roles": ["PAGE_1"]
        }
        r = requests.post(self.__URL + "/api/users", json=new_user, auth=self.__AUTH_ADMIN)
        resp = r.json()
        self.assertEquals(r.status_code, 201)
        self.assertEquals(resp.get("username"), "ferran")

        r = requests.get(self.__URL+"/api/users", auth=self.__AUTH_ADMIN)
        resp = r.json()
        self.assertEquals(len([u for u in resp if u.get("username") == "ferran"]),1)

        r = requests.delete(self.__URL + "/api/users/ferran", auth=self.__AUTH_ADMIN)
        resp = r.json()
        self.assertEquals(r.status_code, 200)

        r = requests.get(self.__URL+"/api/users", auth=self.__AUTH_ADMIN)
        resp = r.json()
        self.assertEquals(len([u for u in resp if u.get("username") == "ferran"]),0)

    def test_update_user(self):
        new_user = {
            "username": "user2",
            "password": "test",
            "roles": ["PAGE_3"]
        }
        r = requests.put(self.__URL + "/api/users", json=new_user, auth=self.__AUTH_ADMIN)
        self.assertEquals(r.status_code, 200)
        r = requests.get(self.__URL+"/api/users/user2", auth=self.__AUTH_ADMIN)
        resp = r.json()
        self.assertTrue("PAGE_3" in resp.get("roles"))

    def test_get_user_that_does_not_exist(self):
        r = requests.get(self.__URL + "/api/users/user5", auth=self.__AUTH_ADMIN)
        self.assertEquals(r.status_code, 404)

    def test_create_user_with_non_admin_user(self):
        new_user = {
            "username": "ferran",
            "password": "test",
            "roles": ["PAGE_1"]
        }
        r = requests.post(self.__URL + "/api/users", json=new_user, auth=self.__AUTH_BASIC_USER)
        self.assertEquals(r.status_code, 403)




if __name__ == '__main__':
    unittest.main(verbosity=2)