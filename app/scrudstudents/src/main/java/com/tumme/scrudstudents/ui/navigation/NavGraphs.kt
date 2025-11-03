package com.tumme.scrudstudents.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
//student ui imports
import com.tumme.scrudstudents.ui.student.StudentListScreen
import com.tumme.scrudstudents.ui.student.StudentFormScreen
import com.tumme.scrudstudents.ui.student.StudentDetailScreen
import com.tumme.scrudstudents.ui.student.StudentHomeScreen
//course ui imports
import com.tumme.scrudstudents.ui.course.CourseDetailScreen
import com.tumme.scrudstudents.ui.course.CourseListScreen
import com.tumme.scrudstudents.ui.course.CourseFormScreen
import com.tumme.scrudstudents.ui.course.CourseEditScreen
//subscribe ui import
import com.tumme.scrudstudents.ui.subscribe.SubscribeFormScreen
import com.tumme.scrudstudents.ui.subscribe.SubscribeListScreen
import com.tumme.scrudstudents.ui.subscribe.SubscribeDetailScreen
import com.tumme.scrudstudents.ui.subscribe.SubscribeEditScreen
//authentication ui import
import com.tumme.scrudstudents.ui.authentication.LoginScreen
import com.tumme.scrudstudents.ui.authentication.RegisterScreen
import com.tumme.scrudstudents.ui.teacher.TeacherHomeScreen
//teacher ui import
import com.tumme.scrudstudents.ui.teacher.TeacherHomeScreen
import com.tumme.scrudstudents.ui.teacher.TeacherStudentList
//teach ui import
import com.tumme.scrudstudents.ui.teaches.TeachListScreen
import com.tumme.scrudstudents.ui.teaches.TeachFormScreen


object Routes {
    //student routes
    const val STUDENT_LIST = "student_list"
    const val STUDENT_FORM = "student_form"
    const val STUDENT_DETAIL = "student_detail/{studentId}"
    const val STUDENT_HOME="student_home/{studentId}"
    //course routes
    const val COURSE_LIST = "course_list"
    const val COURSE_EDIT = "course_edit/{courseId}"
    const val COURSE_FORM = "course_form"
    const val COURSE_DETAIL = "course_detail/{courseId}"
    //subscribe routes
    const val SUBSCRIBE_FORM = "subscribe_form/{studentId}"
    const val SUBSCRIBE_LIST = "subscribe_list/{studentId}"
    const val SUBSCRIBE_DETAIL= "subscribe_detail/{courseId}/{studentId}/{score}"
    const val SUBSCRIBE_EDIT= "subscribe_edit/{courseId}/{studentId}/{score}"
    //authentication routes
    const val LOGIN = "login"
    const val REGISTER = "register"
    //teacher routes
    const val TEACHER_HOME= "teacher_home/{teacherId}"
    const val TEACHER_STUDENTS="teacher_students/{teacherId}"
    //teach routes
    const val TEACH_LIST="teach_list/{teacherId}"
    const val TEACH_FORM="teach_form/{teacherId}"
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()//the app starts in the student list screen
    NavHost(navController, startDestination = Routes.LOGIN) {
        composable(Routes.STUDENT_LIST) {
            StudentListScreen(//different routes reachable from this screen
                onNavigateToForm = { navController.navigate(Routes.STUDENT_FORM) },
                onNavigateToDetail = { id -> navController.navigate("student_detail/$id") },
                onNavigateToSubscriptions = {navController.navigate(Routes.SUBSCRIBE_LIST)},
                onNavigateToCourses = {navController.navigate(Routes.COURSE_LIST)}
            )
        }
        composable(Routes.STUDENT_FORM) {
            StudentFormScreen(//goes back to the student list screen
                onSaved = { navController.popBackStack() }
            )
        }
        composable(//how the navigation passes the useful parameters to the addressed next screen
            route = "student_detail/{studentId}",
            arguments = listOf(navArgument("studentId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("studentId") ?: 0
            StudentDetailScreen(studentId = id, onBack = { navController.popBackStack() })
        }
        composable(Routes.COURSE_LIST) {
            CourseListScreen(//different routes reachable from this screen
                onNavigateToEdit = {id -> navController.navigate("course_edit/$id")},
                onNavigateToForm = { navController.navigate(Routes.COURSE_FORM) },
                onNavigateToDetail = { id -> navController.navigate("course_detail/$id") },
                onNavigateToStudents = {navController.navigate(Routes.STUDENT_LIST)},
                onNavigateToSubscriptions = {navController.navigate(Routes.SUBSCRIBE_LIST)}
            )
        }
        composable(Routes.COURSE_FORM) {
            CourseFormScreen( //goes back to the course list screen
                onSaved = { navController.popBackStack() }
            )
        }
        composable( //how the navigation passes the useful parameters to the addressed next screen
            route = "course_detail/{courseId}",
            arguments = listOf(navArgument("courseId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("courseId") ?: 0
            CourseDetailScreen(courseId = id, onBack = { navController.popBackStack() })
        }
        composable( //how the navigation passes the useful parameters to the addressed next screen
            route = "course_edit/{courseId}",
            arguments = listOf(navArgument("courseId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("courseId") ?: 0
            CourseEditScreen(courseId = id, onSave = { navController.popBackStack() })
        }
        composable( //how the navigation passes the useful parameters to the addressed next screen
            "subscribe_detail/{courseId}/{studentId}/{score}") { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId")?.toInt()
            val studentId = backStackEntry.arguments?.getString("studentId")?.toInt()
            val score = backStackEntry.arguments?.getString("score")?.toFloat()
            SubscribeDetailScreen(courseId = courseId!!, studentId = studentId!!, score = score!!, onBack = { navController.popBackStack() })
        }
        composable( //how the navigation passes the useful parameters to the addressed next screen
            "subscribe_edit/{courseId}/{studentId}/{score}") { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId")?.toInt()
            val studentId = backStackEntry.arguments?.getString("studentId")?.toInt()
            val score = backStackEntry.arguments?.getString("score")?.toFloat()
            SubscribeEditScreen(courseId = courseId!!, studentId = studentId!!, score = score!!, onSave = { navController.popBackStack() })
        }
        composable(Routes.LOGIN) {
        LoginScreen(
            onNavigateToRegister = {navController.navigate(Routes.REGISTER)},
            onLoginSuccess = {role,id->
                if(role=="Teacher"){
                    navController.navigate("teacher_home/$id")
                }else{
                    navController.navigate("student_home/$id")
                }
            }
        )
        }
        composable(Routes.REGISTER) {
            RegisterScreen(
                onNavigateToRegister = {navController.navigate(Routes.LOGIN)},
                onRegistered = {navController.navigate(Routes.LOGIN)}
            )
        }
        composable(
            route = "teacher_home/{teacherId}",
            arguments = listOf(navArgument("teacherId") { type = NavType.IntType })
        ) { backStackEntry ->
            val teacherId = backStackEntry.arguments?.getInt("teacherId") ?: -1
            TeacherHomeScreen(teacherId,
                onNavigateToCourseList = {teacherId->navController.navigate("teach_list/$teacherId")},
                onNavigateToStudentList = {navController.navigate("teacher_students/$teacherId")},
                onLogout={navController.navigate(Routes.LOGIN)}
                )
        }
        composable(
            route="teach_list/{teacherId}",
            arguments= listOf(navArgument("teacherId"){type=NavType.IntType})
        ){
            backStackEntry ->
            val teacherId=backStackEntry.arguments?.getInt("teacherId") ?: -1
            TeachListScreen(
                teacherId,
                onNavigateToForm = {teacherId->navController.navigate("teach_form/$teacherId")},
                onNavigateBack = {navController.navigate("teacher_home/$teacherId")}
            )
        }
        composable(
            route="teach_form/{teacherId}",
            arguments= listOf(navArgument("teacherId"){type=NavType.IntType})
        ){
                backStackEntry ->
            val teacherId=backStackEntry.arguments?.getInt("teacherId") ?: -1
            TeachFormScreen(
                teacherId,
                onSaved = {navController.navigate("teach_list/$teacherId")}
            )
        }
        composable(
            route = "student_home/{studentId}",
            arguments = listOf(navArgument("studentId") { type = NavType.IntType })
        ) { backStackEntry ->
            val studentId = backStackEntry.arguments?.getInt("studentId") ?: -1
            StudentHomeScreen(studentId,
                onNavigateToCourseList = {studentId->navController.navigate("subscribe_list/$studentId")},
                onNavigateToStudentList = {navController.navigate(Routes.STUDENT_LIST)},
                onLogout = {navController.navigate(Routes.LOGIN)}
            )
        }
        composable(
            route="subscribe_list/{studentId}",
            arguments= listOf(navArgument("studentId"){type=NavType.IntType})
        ){
                backStackEntry ->
            val studentId=backStackEntry.arguments?.getInt("studentId") ?: -1
            SubscribeListScreen(
                studentId,
                onNavigateToForm = {studentId->navController.navigate("subscribe_form/$studentId")},
                onNavigateBack = {navController.navigate("student_home/$studentId")}
            )
        }
        composable(
            route="subscribe_form/{studentId}",
            arguments= listOf(navArgument("studentId"){type=NavType.IntType})
        ){
                backStackEntry ->
            val studentId=backStackEntry.arguments?.getInt("studentId") ?: -1
            SubscribeFormScreen(
                studentId,
                onSaved = {navController.navigate("subscribe_list/$studentId")}
            )
        }
        composable(
            route="teacher_students/{teacherId}",
            arguments= listOf(navArgument("teacherId"){type=NavType.IntType})
        ){
                backStackEntry ->
            val teacherId=backStackEntry.arguments?.getInt("teacherId") ?: -1
            TeacherStudentList(
                teacherId,
                onNavigateBack = {navController.navigate("teacher_home/$teacherId")}
            )
        }
    }
}
