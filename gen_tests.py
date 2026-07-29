import os

def gen_user_tests():
    out = ["package com.example.bloodlink", "import org.junit.Test", "import org.junit.Assert.*", "import com.example.bloodlink.data.User", "class UserGeneratedTest {"]
    
    count = 1
    for i in range(110):
        uid = f"uid_{i}"
        name = f"User {i}"
        out.append(f"""
    @Test
    fun testUserAttributes_{count}() {{
        val user = User(uid="{uid}", name="{name}")
        assertEquals("{uid}", user.uid)
        assertEquals("{name}", user.name)
        assertFalse(user.isDonor)
        assertFalse(user.isAdmin)
    }}""")
        count += 1
    out.append("}")
    with open("src/test/java/com/example/bloodlink/UserGeneratedTest.kt", "w") as f:
        f.write("\n".join(out))

def gen_bloodrequest_tests():
    out = ["package com.example.bloodlink", "import org.junit.Test", "import org.junit.Assert.*", "import com.example.bloodlink.data.BloodRequest", "class BloodRequestGeneratedTest {"]
    
    count = 1
    for i in range(110):
        req_id = f"req_{i}"
        urgency = "Emergency" if i % 2 == 0 else "Normal"
        out.append(f"""
    @Test
    fun testBloodRequestAttributes_{count}() {{
        val req = BloodRequest(id="{req_id}", urgency="{urgency}", status="Pending")
        assertEquals("{req_id}", req.id)
        assertEquals("{urgency}", req.urgency)
        assertEquals("Pending", req.status)
        assertEquals("1", req.unitsRequired)
    }}""")
        count += 1
    out.append("}")
    with open("src/test/java/com/example/bloodlink/BloodRequestGeneratedTest.kt", "w") as f:
        f.write("\n".join(out))

gen_user_tests()
gen_bloodrequest_tests()
print("Generated 220 tests successfully.")
