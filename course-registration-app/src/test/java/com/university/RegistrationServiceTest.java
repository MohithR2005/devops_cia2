package com.university;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegistrationServiceTest {

    @Test
    void shouldRegisterStudentForValidCourse() {

        Student student = new Student("S101", "Anita");

        Course course = new Course(
                "CS301",
                "Data Structures",
                4,
                30,
                true,
                null
        );

        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);

        assertTrue(result);
        assertTrue(student.isRegistered("CS301"));
        assertEquals(4, student.getTotalCredits());
        assertEquals(1, course.getCurrentEnrollment());
    }

    @Test
    void shouldRejectInactiveCourse() {

        Student student = new Student("S101", "Anita");

        Course course = new Course(
                "CS301",
                "Data Structures",
                4,
                30,
                false,
                null
        );

        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);

        assertFalse(result);
        assertFalse(student.isRegistered("CS301"));
    }

    @Test
    void shouldRejectMissingPrerequisite() {

        Student student = new Student("S101", "Anita");

        Course course = new Course(
                "CS401",
                "Advanced Algorithms",
                4,
                30,
                true,
                "CS301"
        );

        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);

        assertFalse(result);
        assertFalse(student.isRegistered("CS401"));
    }

    @Test
    void shouldAllowRegistrationWhenPrerequisiteIsCompleted() {

        Student student = new Student("S101", "Anita");
        student.addCompletedPrerequisite("CS301");

        Course course = new Course(
                "CS401",
                "Advanced Algorithms",
                4,
                30,
                true,
                "CS301"
        );

        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);

        assertTrue(result);
        assertTrue(student.isRegistered("CS401"));
    }

    @Test
    void shouldRejectDuplicateRegistration() {

        Student student = new Student("S101", "Anita");

        Course course = new Course(
                "CS301",
                "Data Structures",
                4,
                30,
                true,
                null
        );

        RegistrationService service = new RegistrationService();

        assertTrue(service.register(student, course));

        boolean secondAttempt = service.register(student, course);

        assertFalse(secondAttempt);
        assertEquals(4, student.getTotalCredits());
        assertEquals(1, course.getCurrentEnrollment());
    }

    @Test
    void shouldRejectRegistrationWhenCreditLimitIsExceeded() {

        Student student = new Student("S101", "Anita");

        // Simulate 20 existing credits
        student.addRegistration("CS101", 4);
        student.addRegistration("CS102", 4);
        student.addRegistration("CS201", 4);
        student.addRegistration("CS202", 4);
        student.addRegistration("CS203", 4);

        Course course = new Course(
                "CS301",
                "Data Structures",
                4,
                30,
                true,
                null
        );

        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);

        assertFalse(result);
        assertFalse(student.isRegistered("CS301"));
    }
    
@Test
void shouldRejectRegistrationWhenCourseIsAtCapacity() {

    Student student = new Student("S102", "Rahul");

    Course course = new Course(
            "CS302",
            "Operating Systems",
            4,
            1,
            true,
            null
    );

    // Fill the only available seat.
    course.incrementEnrollment();

    RegistrationService service = new RegistrationService();

    boolean result = service.register(student, course);

    assertFalse(result);
    assertFalse(student.isRegistered("CS302"));
    assertEquals(0, student.getTotalCredits());
    assertEquals(1, course.getCurrentEnrollment());
}

@Test
void shouldRegisterStudentWhenExactlyOneSeatRemains() {

    Student student = new Student("S103", "Priya");

    Course course = new Course(
            "CS303",
            "Computer Networks",
            4,
            1,
            true,
            null
    );

    RegistrationService service = new RegistrationService();

    boolean result = service.register(student, course);

    assertTrue(result);
    assertTrue(student.isRegistered("CS303"));
    assertEquals(4, student.getTotalCredits());
    assertEquals(1, course.getCurrentEnrollment());
}

@Test
void shouldAcceptCourseWithOneCredit() {
    Student student = new Student("S104", "Arun");
    Course course = new Course(
            "CS304", "One Credit Course", 1, 30, true, null);

    RegistrationService service = new RegistrationService();

    assertTrue(service.register(student, course));
    assertTrue(student.isRegistered("CS304"));
    assertEquals(1, student.getTotalCredits());
    assertEquals(1, course.getCurrentEnrollment());
}

@Test
void shouldAcceptCourseWithSixCredits() {
    Student student = new Student("S105", "Meera");
    Course course = new Course(
            "CS305", "Six Credit Course", 6, 30, true, null);

    RegistrationService service = new RegistrationService();

    assertTrue(service.register(student, course));
    assertTrue(student.isRegistered("CS305"));
    assertEquals(6, student.getTotalCredits());
    assertEquals(1, course.getCurrentEnrollment());
}

@Test
void shouldRejectCourseWithZeroCreditsWithoutChangingState() {
    Student student = new Student("S106", "Kiran");
    Course course = new Course(
            "CS306", "Zero Credit Course", 0, 30, true, null);

    RegistrationService service = new RegistrationService();

    assertFalse(service.register(student, course));
    assertFalse(student.isRegistered("CS306"));
    assertEquals(0, student.getTotalCredits());
    assertEquals(0, course.getCurrentEnrollment());
}

@Test
void shouldRejectCourseWithSevenCreditsWithoutChangingState() {
    Student student = new Student("S107", "Divya");
    Course course = new Course(
            "CS307", "Seven Credit Course", 7, 30, true, null);

    RegistrationService service = new RegistrationService();

    assertFalse(service.register(student, course));
    assertFalse(student.isRegistered("CS307"));
    assertEquals(0, student.getTotalCredits());
    assertEquals(0, course.getCurrentEnrollment());
}

@Test
void shouldRejectCourseWithNegativeCreditsWithoutChangingState() {
    Student student = new Student("S108", "Vijay");
    Course course = new Course(
            "CS308", "Negative Credit Course", -1, 30, true, null);

    RegistrationService service = new RegistrationService();

    assertFalse(service.register(student, course));
    assertFalse(student.isRegistered("CS308"));
    assertEquals(0, student.getTotalCredits());
    assertEquals(0, course.getCurrentEnrollment());
}

}