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

export default configureStore({
    reducer: {
        userInOut: userInOut.reducer
    }
})