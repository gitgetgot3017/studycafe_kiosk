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
    initialState: 'GUEST',
    reducers: {
        changeMemberGrade(state, action) {
            return state = action.payload;
        }
    }
})
export let { changeMemberGrade } = memberGrade.actions;

let orderItemId = createSlice({
    name: 'orderItemId',
    initialState: 0,
    reducers: {
        setOrderItemId(state, action) {
            return state = action.payload;
        }
    }
})
export let { setOrderItemId } = orderItemId.actions;

let orderItemName = createSlice({
    name: 'orderItemName',
    initialState: '',
    reducers: {
        setOrderItemName(state, action) {
            return state = action.payload;
        }
    }
})
export let { setOrderItemName } = orderItemName.actions;

let orderItemPrice = createSlice({
    name: 'orderItemPrice',
    initialState: 0,
    reducers: {
        setOrderItemPrice(state, action) {
            return state = action.payload;
        }
    }
})
export let { setOrderItemPrice } = orderItemPrice.actions;

let uuid = createSlice({
    name: 'uuid',
    initialState: '',
    reducers: {
        setUuid(state, action) {
            return state = action.payload;
        }
    }
})
export let { setUuid } = uuid.actions;

export default configureStore({
    reducer: {
        userInOut: userInOut.reducer,
        login: login.reducer,
        memberInfo: memberInfo.reducer,
        memberGrade: memberGrade.reducer,
        orderItemId: orderItemId.reducer,
        orderItemName: orderItemName.reducer,
        orderItemPrice: orderItemPrice.reducer,
        uuid: uuid.reducer
    }
})