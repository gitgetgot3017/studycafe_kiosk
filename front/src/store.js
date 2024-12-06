import {configureStore, createSlice} from '@reduxjs/toolkit'

let userInOut = createSlice({
    name: 'userInOut',
    initialState: false,
    reducers: {
        changeUserInOut(state, action) {
            return state = action.payload;
        }
    }
})
export let { changeUserInOut } = userInOut.actions;

let login = createSlice({
    name: 'login',
    initialState: false,
    reducers: {
        changeLoginStatus(state, action) {
            return state = action.payload;
        }
    }
})
export let { changeLoginStatus } = login.actions;

let memberInfo = createSlice({
    name: 'memberInfo',
    initialState: [],
    reducers: {
        getMemberInfo(state, action) {
            return state = action.payload;
        }
    }
})
export let { getMemberInfo } = memberInfo.actions;

let memberGrade = createSlice({
    name: 'memberGrade',
    initialState: '',
    reducers: {
        changeMemberGrade(state, action) {
            return state = action.payload;
        }
    }
})
export let { changeMemberGrade } = memberGrade.actions;

export default configureStore({
    reducer: {
        userInOut: userInOut.reducer,
        login: login.reducer,
        memberInfo: memberInfo.reducer,
        memberGrade: memberGrade.reducer
    }
})