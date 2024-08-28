import json

def string_to_json(str):   
    return json.loads(str)
    
def compare_json(dict1, dict2): 
    print(dict1,dict2)
    print(type(dict1),type(dict2)) 
    if isinstance(dict1,list) and isinstance(dict2,list):
        if dict1 == dict2:
            return True
        flag=False
        for j in dict2:
            #print(j)
            for i in dict1:
                count = 0
                for key,value in j.items():
                     #print(key,value)
                    if i.get(key) == value :
                        print(i.get(key),value)
                        count+=1
                if count== len(j):
                    flag=True
                    break
                else:
                    flag = False
        return flag
    if isinstance(dict1,dict) and isinstance(dict2,dict):
        i=0
        for key,value in dict2.items():
            if dict1.get(key) == value :
                i+=1
        if i== len(dict2):
            return True
    return False
          

#print(string_to_json({'a':100}))
# if __name__ == "__main__":
#     main()
